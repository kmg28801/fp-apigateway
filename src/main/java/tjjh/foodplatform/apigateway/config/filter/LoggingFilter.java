package tjjh.foodplatform.apigateway.config.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {
    
    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        /*return (exchange, chain) -> {
            ServerHttpRequest serverHttpRequest = exchange.getRequest();
            ServerHttpResponse serverHttpResponse = exchange.getResponse();

            log.info("Logging Filter baseMessage: {} ", config.getBaseMessage());

            if(config.isPreLogger()) {
                log.info("Logging Filter Start : request ID -> {}", serverHttpRequest.getId());
            }

            // Custom Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if(config.isPostLogger()) {
                    log.info("Logging Filter End : response Code -> {}", serverHttpResponse.getStatusCode());
                }

            }));
        };*/

        GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest serverHttpRequest = exchange.getRequest();
            ServerHttpResponse serverHttpResponse = exchange.getResponse();

            log.info("Logging Filter baseMessage: {} ", config.getBaseMessage());

            if(config.isPreLogger()) {
                log.info("Logging PRE Filter : request ID -> {}", serverHttpRequest.getId());
            }

            // Custom Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if(config.isPostLogger()) {
                    log.info("Logging POST Filter : response Code -> {}", serverHttpResponse.getStatusCode());
                }

            }));
        }, Ordered.LOWEST_PRECEDENCE);
        /* 필터의 우선 순위를 정함 Ordered.HIGHEST_PRECEDENCE이면 Global Filter보다 우선순위가 높아서 사실상 Global이 되어버림
        LOWEST_PRECEDENCE를 주게되면 가장 안쪽에서 호출됨.
        Spring WebFlux를 사용하게되면 (MVC 말고..) 비동기 웹 서버를 사용하게됨 (Netty) ServerHttpRequest, ServerHttpResponse가 WebFlux에서 지원하는 인터페이스
         */

        return filter;
    }

    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
