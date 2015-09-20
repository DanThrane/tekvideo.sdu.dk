dataSource {
    pooled = true
    jmxExport = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
    dialect = "dk.sdu.tekvideo.ImprovedH2Dialect"
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
//    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
    singleSession = true // configure OSIV singleSession mode
    flush.mode = 'manual' // OSIV session flush mode outside of transactional context
    /*
     * A bug with table inheritance is causing errors. To get rid of these reload has to be set to false
     * See https://jira.grails.org/browse/GRAILS-8805
     */
    reload = false
}

// environment specific settings
environments {
    development {
        dataSource {
            logSql = true
            driverClassName = "org.postgresql.Driver"
            dialect = "org.hibernate.dialect.PostgreSQLDialect"
            url = "jdbc:postgresql://localhost:5432/tekvideo-dev"
            username = "devuser"
            password = "devpassword"

        }
    }
    test {
        dataSource {
            logSql = true
            dbCreate = "create-drop"
            driverClassName = "org.postgresql.Driver"
            dialect = "org.hibernate.dialect.PostgreSQLDialect"
            url = "jdbc:postgresql://localhost:5432/tekvideo-test"
            username = "devuser"
            password = "devpassword"
        }
    }
    production {
        dataSource {
            logSql = true
            driverClassName = "org.postgresql.Driver"
            dialect = "org.hibernate.dialect.PostgreSQLDialect"
            url = "jdbc:postgresql://localhost:5432/tekvideo"
        }
    }
}
