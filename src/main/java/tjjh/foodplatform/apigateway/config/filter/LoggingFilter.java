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
        /* ????????? ?????? ????????? ?????? Ordered.HIGHEST_PRECEDENCE?????? Global Filter?????? ??????????????? ????????? ????????? Global??? ????????????
        LOWEST_PRECEDENCE??? ???????????? ?????? ???????????? ?????????.
        Spring WebFlux??? ?????????????????? (MVC ??????..) ????????? ??? ????????? ??????????????? (Netty) ServerHttpRequest, ServerHttpResponse??? WebFlux?????? ???????????? ???????????????
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
