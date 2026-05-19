package mocp;

import mocp.ast.*;

public class ASTBuilder extends MOCPBaseVisitor<ASTNode> {

    // ==========================================
    // REGRAS ESTRUTURAIS / NÍVEL SUPERIOR
    // ==========================================

    @Override
    public ASTNode visitPrograma(MOCPParser.ProgramaContext ctx) {
        ProgramaNode programa = new ProgramaNode();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ASTNode node = visit(ctx.getChild(i));
            if (node != null) {
                programa.addNode(node);
            }
        }
        return programa;
    }

    @Override
    public ASTNode visitDec_variavel(MOCPParser.Dec_variavelContext ctx) {
        String tipo = ctx.tipo().getText();
        DeclaracaoNode decNode = new DeclaracaoNode(tipo);
        for (MOCPParser.Dec_itemContext itemCtx : ctx.dec_item()) {
            ASTNode item = visit(itemCtx);
            if (item != null) {
                decNode.addItem(item);
            }
        }
        return decNode;
    }

    @Override
    public ASTNode visitDec_item(MOCPParser.Dec_itemContext ctx) {
        String id = ctx.ID().getText();
        DeclaradorNode declarador = new DeclaradorNode(id);

        if (ctx.inicializador() != null) {
            ASTNode initNode = visit(ctx.inicializador());
            declarador.setInicializador(initNode);
        }
        return declarador;
    }

    @Override
    public ASTNode visitInicializador(MOCPParser.InicializadorContext ctx) {
        // Se for expressão simples, delega. Se for lista entre {}, pode criar um nó composto ou iterar.
        if (ctx.ABRE_CHAV() != null) {
            // Para simplificar vetores em blocos:
            AfirmacaoCompostaNode listaInit = new AfirmacaoCompostaNode();
            for (MOCPParser.ExpressaoContext exprCtx : ctx.expressao()) {
                listaInit.addInstrucao(visit(exprCtx));
            }
            return listaInit;
        }
        return visit(ctx.expressao(0));
    }

    @Override
    public ASTNode visitPrototipo(MOCPParser.PrototipoContext ctx) {
        String tipo = ctx.tipo().getText();
        String nome = ctx.ID() != null ? ctx.ID().getText() : ctx.PRINCIPAL().getText();
        PrototipoNode prototipo = new PrototipoNode(tipo, nome);

        if (ctx.prototipo_params() != null) {
            // Recolhemos os tipos passados no protótipo para carregar o nó
            MOCPParser.Prototipo_paramsContext pCtx = ctx.prototipo_params();
            if (pCtx.param_tipo() != null) {
                for (MOCPParser.Param_tipoContext pt : pCtx.param_tipo()) {
                    prototipo.addTipoParametro(pt.tipo().getText() + (pt.ABRE_RET() != null ? "[]" : ""));
                }
            }
        }
        return prototipo;
    }

    @Override
    public ASTNode visitPrototipo_params(MOCPParser.Prototipo_paramsContext ctx) {
        // Mapeia: param_tipo (VIRGULA param_tipo)* | T_VAZIO
        return null;
    }

    @Override
    public ASTNode visitParam_tipo(MOCPParser.Param_tipoContext ctx) {
        // Mapeia: tipo (ABRE_RET FECHA_RET)?
        return null;
    }

    @Override
    public ASTNode visitFuncao(MOCPParser.FuncaoContext ctx) {
        String tipo = ctx.tipo().getText();
        String nome = ctx.ID() != null ? ctx.ID().getText() : ctx.PRINCIPAL().getText();

        FuncaoNode funcao = new FuncaoNode(tipo, nome);

        // ATUALIZAÇÃO: Capturar os parâmetros da assinatura se existirem
        if (ctx.parametros() != null) {
            funcao.setParametros(visit(ctx.parametros()));
        }

        if (ctx.bloco() != null) {
            funcao.setBloco(visit(ctx.bloco()));
        }

        return funcao;
    }

    @Override
    public ASTNode visitParametros(MOCPParser.ParametrosContext ctx) {
        // Se a regra for T_VAZIO ou não tiver filhos, ignora
        if (ctx.T_VAZIO() != null || ctx.param_dec() == null) return null;

        // Mapeia os parâmetros para uma lista estruturada (reutilizando o AfirmacaoCompostaNode como contentor genérico)
        AfirmacaoCompostaNode listaParams = new AfirmacaoCompostaNode();
        for (MOCPParser.Param_decContext pCtx : ctx.param_dec()) {
            listaParams.addInstrucao(visit(pCtx));
        }
        return listaParams;
    }

    @Override
    public ASTNode visitParam_dec(MOCPParser.Param_decContext ctx) {
        String tipo = ctx.tipo().getText();
        String id = ctx.ID().getText();
        boolean esVetor = ctx.ABRE_RET() != null;
        return new ParametroNode(tipo, id, esVetor);
    }

    @Override
    public ASTNode visitBloco(MOCPParser.BlocoContext ctx) {
        AfirmacaoCompostaNode bloco = new AfirmacaoCompostaNode();
        for (MOCPParser.InstrucaoContext instrCtx : ctx.instrucao()) {
            bloco.addInstrucao(visit(instrCtx)); // Visita cada instrução lá dentro
        }
        return bloco;
    }

    // ==========================================
    // REGRAS DE INSTRUÇÃO (STATEMENTS)
    // ==========================================

    @Override
    public ASTNode visitInstrucao(MOCPParser.InstrucaoContext ctx) {
        // 1. Verificar o RETORNAR primeiro!
        if (ctx.RETORNAR() != null) {
            RetornarNode ret = new RetornarNode();
            if (ctx.expressao() != null) {
                ret.setExpressao(visit(ctx.expressao()));
            }
            return ret;
        }

        // 2. Só depois verifica o resto
        if (ctx.dec_variavel() != null) return visit(ctx.dec_variavel());
        if (ctx.expressao() != null) return visit(ctx.expressao());
        if (ctx.instrucao_se() != null) return visit(ctx.instrucao_se());
        if (ctx.instrucao_enquanto() != null) return visit(ctx.instrucao_enquanto());
        if (ctx.instrucao_para() != null) return visit(ctx.instrucao_para());

        return null;
    }

    @Override
    public ASTNode visitInstrucao_se(MOCPParser.Instrucao_seContext ctx) {
        // Instancia o teu SeNode (ajusta os métodos conforme a tua classe SeNode.java)
        SeNode seNode = new SeNode();

        // Configura a condição: (x > 5)
        seNode.setCondicao(visit(ctx.expressao()));

        // Configura o bloco do 'se'
        seNode.setBlocoSe(visit(ctx.bloco(0)));

        // Se existir o bloco 'senao' (bloco(1))
        if (ctx.SENAO() != null && ctx.bloco().size() > 1) {
            seNode.setBlocoSenao(visit(ctx.bloco(1)));
        }

        return seNode;
    }

    @Override
    public ASTNode visitInstrucao_enquanto(MOCPParser.Instrucao_enquantoContext ctx) {
        EnquantoNode node = new EnquantoNode();
        node.setCondicao(visit(ctx.expressao()));
        node.setBloco(visit(ctx.bloco()));
        return node;
    }

    @Override
    public ASTNode visitInstrucao_para(MOCPParser.Instrucao_paraContext ctx) {
        ParaNode node = new ParaNode();

        // No ANTLR, se temos múltiplas ocorrências opcionais da mesma regra, avaliamos por índice de aparição:
        // expressao? PONTO_VIRG expressao? PONTO_VIRG expressao?
        ASTNode init = null, cond = null, inc = null;
        int count = ctx.expressao().size();

        // Lógica segura para determinar quais expressões foram preenchidas no cabeçalho do para(;;)
        if (count == 3) {
            init = visit(ctx.expressao(0));
            cond = visit(ctx.expressao(1));
            inc = visit(ctx.expressao(2));
        } else if (count == 2) {
            if (ctx.getChild(2).getText().equals(";")) { // Falta a do meio ou a última?
                init = visit(ctx.expressao(0));
                inc = visit(ctx.expressao(1));
            } else {
                init = visit(ctx.expressao(0));
                cond = visit(ctx.expressao(1));
            }
        } else if (count == 1) {
            cond = visit(ctx.expressao(0)); // Assume condição por padrão
        }

        node.setComponentes(init, cond, inc, visit(ctx.bloco()));
        return node;
    }

    @Override
    public ASTNode visitLista_args(MOCPParser.Lista_argsContext ctx) {
        if (ctx.expressao() == null) return null;

        AfirmacaoCompostaNode listaArgs = new AfirmacaoCompostaNode();
        for (MOCPParser.ExpressaoContext eCtx : ctx.expressao()) {
            listaArgs.addInstrucao(visit(eCtx));
        }
        return listaArgs;
    }

    // ==========================================
    // REGRAS DE EXPRESSÃO (RÓTULOS #)
    // ==========================================

    @Override
    public ASTNode visitExprParenteses(MOCPParser.ExprParentesesContext ctx) {
        return visit(ctx.expressao());
    }

    @Override
    public ASTNode visitExprCast(MOCPParser.ExprCastContext ctx) {
        // Mapeia: ABRE_PAR tipo FECHA_PAR expressao
        String tipoAlvo = ctx.tipo().getText();
        ASTNode expressaoOriginal = visit(ctx.expressao());

        // Podem modelar isto usando o OpUnNode para indicar uma conversão unária de tipo:
        return new OpUnNode("(cast para " + tipoAlvo + ")", expressaoOriginal);
    }

    @Override
    public ASTNode visitExprMenosUnario(MOCPParser.ExprMenosUnarioContext ctx) {
        return new OpUnNode("-", visit(ctx.expressao()));
    }

    @Override
    public ASTNode visitExprNao(MOCPParser.ExprNaoContext ctx) {
        return new OpUnNode("!", visit(ctx.expressao()));
    }

    @Override
    public ASTNode visitExprMultDiv(MOCPParser.ExprMultDivContext ctx) {
        String op = ctx.getChild(1).getText();
        return new OpBinNode(op, visit(ctx.expressao(0)), visit(ctx.expressao(1)));
    }

    @Override
    public ASTNode visitExprSomaSub(MOCPParser.ExprSomaSubContext ctx) {
        String op = ctx.getChild(1).getText();
        return new OpBinNode(op, visit(ctx.expressao(0)), visit(ctx.expressao(1)));
    }

    @Override
    public ASTNode visitExprRelacional(MOCPParser.ExprRelacionalContext ctx) {
        ASTNode esquerda = visit(ctx.expressao(0));  // Nó do ID 'x'
        ASTNode direita = visit(ctx.expressao(1));   // Nó do LiteralInt '5'
        String operador = ctx.getChild(1).getText(); // Captura ">", "<", ">=", "<="

        return new OpBinNode(operador, esquerda, direita);
    }

    @Override
    public ASTNode visitExprIgualdade(MOCPParser.ExprIgualdadeContext ctx) {
        String op = ctx.getChild(1).getText();
        return new OpBinNode(op, visit(ctx.expressao(0)), visit(ctx.expressao(1)));
    }

    @Override
    public ASTNode visitExprE(MOCPParser.ExprEContext ctx) {
        return new OpBinNode("&&", visit(ctx.expressao(0)), visit(ctx.expressao(1)));
    }

    @Override
    public ASTNode visitExprOu(MOCPParser.ExprOuContext ctx) {
        return new OpBinNode("||", visit(ctx.expressao(0)), visit(ctx.expressao(1)));
    }

    @Override
    public ASTNode visitExprVetorAtrib(MOCPParser.ExprVetorAtribContext ctx) {
        // Atribuição a vetor: ID[expr] = expr
        String id = ctx.ID().getText();
        ASTNode indice = visit(ctx.expressao(0));
        ASTNode valor = visit(ctx.expressao(1));

        // Criamos um operador binário de atribuição (=) onde o lado esquerdo é o acesso ao vetor
        return new OpBinNode("=", new AcessoVetorNode(id, indice), valor);
    }

    @Override
    public ASTNode visitExprVetor(MOCPParser.ExprVetorContext ctx) {
        return new AcessoVetorNode(ctx.ID().getText(), visit(ctx.expressao()));
    }

    @Override
    public ASTNode visitExprAtribuicao(MOCPParser.ExprAtribuicaoContext ctx) {
        String id = ctx.ID().getText();
        return new OpBinNode("=", new IDNode(id), visit(ctx.expressao()));
    }

    @Override
    public ASTNode visitExprChamadaFuncao(MOCPParser.ExprChamadaFuncaoContext ctx) {
        String nome = ctx.ID().getText();
        ChamadaFuncaoNode chamada = new ChamadaFuncaoNode(nome);

        if (ctx.lista_args() != null) {
            for (MOCPParser.ExpressaoContext exprCtx : ctx.lista_args().expressao()) {
                chamada.addArgumento(visit(exprCtx));
            }
        }
        return chamada;
    }

    @Override
    public ASTNode visitExprInt(MOCPParser.ExprIntContext ctx) {
        // Retorna um nó de Literal Inteiro (usa a tua classe LiteralIntNode.java)
        return new LiteralIntNode(ctx.INT_VAL().getText());
    }

    @Override
    public ASTNode visitExprReal(MOCPParser.ExprRealContext ctx) {
        // Agora que a vossa classe LiteralRealNode já existe, podemos instanciá-la:
        return new LiteralRealNode(ctx.FLOAT_VAL().getText());
    }

    @Override
    public ASTNode visitExprString(MOCPParser.ExprStringContext ctx) {
        return new LiteralStringNode(ctx.STRING_VAL().getText());
    }

    @Override
    public ASTNode visitExprChar(MOCPParser.ExprCharContext ctx) {
        // Pode usar o LiteralStringNode ou criar um CharNode semelhante se preferir
        return new LiteralStringNode(ctx.CHAR_VAL().getText());
    }

    @Override
    public ASTNode visitExprId(MOCPParser.ExprIdContext ctx) {
        // Retorna um nó de Identificador (usa a tua classe IDNode.java)
        return new IDNode(ctx.ID().getText());
    }
}