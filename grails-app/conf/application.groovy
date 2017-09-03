grails.config.locations = ["file:${System.getProperty("user.home")}/.grails/tekvideo-config.properties"]

grails {
    mail {
        host = "smtp.gmail.com"
        port = 465
        username = "sdu.tekvideo@gmail.com"
        props = ["mail.smtp.auth":"true",
                 "mail.smtp.socketFactory.port":"465",
                 "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
                 "mail.smtp.ssl.enable": "true",
                 "mail.smtp.socketFactory.fallback":"false"]
    }
}

// environment specific settings
environments {
	development {
		dataSource {
			logSql = true
			driverClassName = "org.postgresql.Driver"
			dialect = "org.hibernate.dialect.PostgreSQLDialect"
			url = "jdbc:postgresql://localhost:5432/tekvideo-dev"
		}
	}
	test {
		dataSource {
			logSql = false
			dbCreate = "create-drop"
			driverClassName = "org.postgresql.Driver"
			dialect = "org.hibernate.dialect.PostgreSQLDialect"
			url = "jdbc:postgresql://localhost:5432/tekvideo-test"
		}
	}
	production {
		dataSource {
			logSql = false
			driverClassName = "org.postgresql.Driver"
			dialect = "org.hibernate.dialect.PostgreSQLDialect"
			url = "jdbc:postgresql://localhost:5432/tekvideo"
		}
	}
}

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'dk.sdu.tekvideo.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'dk.sdu.tekvideo.UserRole'
grails.plugin.springsecurity.authority.className = 'dk.sdu.tekvideo.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/index.gsp',      access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/**',             filters: 'JOINED_FILTERS']
]

markdown.autoLinks = true

// SDU SSO
grails.plugin.springsecurity.cas.loginUri = '/login'
//grails.plugin.springsecurity.cas.serviceUrl = "${grails.serverURL}/j_spring_cas_security_check"
//grails.plugin.springsecurity.cas.proxyCallbackUrl = "${grails.serverURL}/secure/receptor"
//grails.plugin.springsecurity.cas.serverUrlPrefix = 'https://sso.sdu.dk/'
//grails.plugin.springsecurity.cas.casServerUrlPrefix = 'https://sso.sdu.dk/'
//grails.plugin.springsecurity.cas.proxyReceptorUrl = '/secure/receptor'
grails.plugin.springsecurity.cas.serviceUrl = "https://localhost:8443/login/cas"
grails.plugin.springsecurity.cas.serverUrlPrefix = 'https://sso.sdu.dk/'
grails.plugin.springsecurity.cas.proxyCallbackUrl = "https://localhost:8443/secure/receptor"
grails.plugin.springsecurity.cas.proxyReceptorUrl = '/secure/receptor'
