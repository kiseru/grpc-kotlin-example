package ru.grpc.simple.server.grpcserver;

import io.grpc.Metadata;
import io.grpc.Server;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import ru.grpc.simple.server.grpcserver.service.UserService;

import java.io.IOException;

public class GrpcServerApplication {

    public static void main(String[] args) throws InterruptedException, IOException {

        Server server = NettyServerBuilder.forPort(9091)
                .addService(new UserService())
                .intercept(new TestServerInterceptor())
                .build();

        server.start();

        System.out.println(String.format("gRPC Server started on port: %d", server.getPort()));

        server.awaitTermination();
    }

    static class TestServerInterceptor implements ServerInterceptor {
        @Override
        public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
                ServerCall<ReqT, RespT> call,
                Metadata headers,
                ServerCallHandler<ReqT, RespT> serverCallHandler) {

            System.out.println("Recieved following metadata: " + headers);
            return serverCallHandler.startCall(call, headers);
        }
    }
}
