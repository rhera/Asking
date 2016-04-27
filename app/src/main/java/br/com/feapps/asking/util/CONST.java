package br.com.feapps.asking.util;

/**
 * Created by F.Einstein on 003, 3/1/2016.
 */
public final class CONST {
    //Tabela usuário
    public static String USERTABID = "id";
    public static String USERTABNOME = "nome";
    public static String USERTABTELEFONE = "telefone";
    public static String USERTABEMAIL = "email";
    public static String USERTABNASCIMENTO = "nascimento";
    public static String USERTABLOGIN = "login";
    public static String USERTABSENHA = "senha";
    public static String USERTABTIPO = "tipo";
    public static String USERTABFOTO = "foto";

    //Tabela area
    public static String AREATABID = "id";
    public static String AREATABNOME = "nome";

    //Tabela sub área
    public static String SUBAREATABID = "id";
    public static String SUBAREATABNOME = "nome";
    public static String SUBAREATABIDAREA = "area_id";

    //Tabela sala
    public static String SALATABID = "id";
    public static String SALATABNOME = "nome";
    public static String SALATABTIPO = "tipo";
    public static String SALATABSENHA = "senha";
    public static String SALATABAREA = "area_id";
    public static String SALATABSUBAREA = "subarea_id";
    public static String SALATABUSER = "usuario_criador_id";

    //Constantes de ranking
    public static int RANKING = 0;
    public static int RANKINGTEMP = 1;
    public static int RANKINGORDERTEMPO = 3;
    public static int RANKINGORDER5ACERTOS = 4;

    //Erros de banco de dados
    public static int KEYERROR = 0;
    public static int SUCESS = 1;
    public static int ERROR = 2;
    public static int USERLOGINEXIST = 3;
    public static int USEREMAILEXIST = 4;
    public static int ERRORWEBSERVER = 5;
    public static int SALAEXIST = 6;

    //Informações salvas no activity
    public static String EXTRA_SALA = "sala";
}
