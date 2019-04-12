package bo.alxmcr.app.constants;

/**
 * Created by JoseCoca-i7 on 02/05/2015.
 */
public class ConstantsSQLLiteDB {
    public static final String NAME_DATABASE = "vocabulary.db";
    public static final int NUMBER_VERSION_DATABASE = 1;

    public static final String TABLE_WORD = "word";
    public static final String TABLE_MEANING = "meaning";

    public static final String SQL_CREATE_TABLE_WORD = "CREATE TABLE word (\n" +
            "    id character varying(50) NOT NULL,\n" +
            "    creationdate date NOT NULL,\n" +
            "    creationtime time without time zone NOT NULL,\n" +
            "    modificationdate date NOT NULL,\n" +
            "    modificationtime time without time zone NOT NULL,\n" +
            "    status character varying(50) NOT NULL,\n" +
            "    text character varying(50) NOT NULL,\n" +
            "    PRIMARY KEY (id)\n" +
            ")";

}
