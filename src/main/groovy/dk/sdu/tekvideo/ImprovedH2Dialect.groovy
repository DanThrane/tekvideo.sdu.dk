package dk.sdu.tekvideo

import org.hibernate.dialect.H2Dialect

class ImprovedH2Dialect extends H2Dialect {
    @Override
    String getDropSequenceString(String sequenceName) {
        return "drop sequence if exists " + sequenceName
    }

    @Override
    boolean dropConstraints() {
        return false
    }
}