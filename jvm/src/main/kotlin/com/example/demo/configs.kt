import com.example.demo.http.pipeline.AuthenticationInterceptor // ktlint-disable filename
import com.example.demo.http.pipeline.UserArgumentResolver
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import repository.implementations.mappers.UserMapper
import java.sql.Connection

@Suppress("unused")
@Configuration
class Configuration {

    @Bean
    fun getNewConnection(): Connection? {
        val dataSource = PGSimpleDataSource()
        val jdbcDatabaseURL = System.getenv("JDBC_DATABASE_URL")
        dataSource.setURL(jdbcDatabaseURL)

        return dataSource.connection
    }

    @Bean
    fun createUserMapper() = UserMapper()
}

@Suppress("unused")
@Configuration
class PipelineConfigure(
    val authenticationInterceptor: AuthenticationInterceptor,
    val userArgumentResolver: UserArgumentResolver,
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authenticationInterceptor)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(userArgumentResolver)
    }
}
