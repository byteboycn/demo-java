package cn.byteboy.demo.spring.gateway.handler;

import org.springframework.web.reactive.handler.AbstractHandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author hongshaochuan
 * @date 2021/10/24
 */
public class RouteHandlerMapping extends AbstractHandlerMapping {

    @Override
    protected Mono<?> getHandlerInternal(ServerWebExchange exchange) {
        return Mono.empty();
    }
}
