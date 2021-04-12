package com.gateway.config.database;

import org.hibernate.dialect.Oracle10gDialect;

@SuppressWarnings("unused")
public class CustomOracleDialect extends Oracle10gDialect {
    @Override
    public String getQuerySequencesString() {
        return "select * from user_sequences";
    }
//    @Override
//    public SequenceInformationExtractor getSequenceInformationExtractor() {
//        return AppSequenceInformationExtractor.INSTANCE;
//    }
}
