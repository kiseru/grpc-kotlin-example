package ru.grpc.simple.client.grpcclient;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import ru.grpc.simple.proto.CommonResponse;
import ru.grpc.simple.proto.LoginRequest;
import ru.grpc.simple.proto.LogoutRequest;
import ru.grpc.simple.proto.UserGrpc;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

public class GrpcClientApplication {

    public static void main(String[] args) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("0.0.0.0", 9091)
                .usePlaintext()
                .intercept(new TestClientInterceptor())
                .build();

        UserGrpc.UserBlockingStub stub = UserGrpc.newBlockingStub(channel);

        LoginRequest loginRequest = LoginRequest.newBuilder()
                .setUsername("userTestLogin")
                .setPassword("qwe123")
                .build();
        CommonResponse response = stub.login(loginRequest);
        System.out.println(String.format("Login -> Response code: %d, message: %s",
                                         response.getResponseCode(),
                                         response.getResponseMessage()));
        System.out.println(String.format("Login -> All fields: %s", response.getAllFields()));

        LogoutRequest logoutRequest = LogoutRequest.newBuilder()
                .setUsername("userTestLogin")
                .build();
        response = stub.logout(logoutRequest);
        System.out.println(String.format("Logout -> Response code: %d, message: %s",
                                         response.getResponseCode(),
                                         response.getResponseMessage()));
        System.out.println(String.format("Logout -> All fields: %s", response.getAllFields()));
    }

    static class TestClientInterceptor implements ClientInterceptor {
        @Override
        public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor,
                                                                   CallOptions callOptions,
                                                                   Channel channel) {
            return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(channel.newCall(methodDescriptor, callOptions)) {
                @Override
                public void start(Listener<RespT> responseListener, Metadata headers) {
                    System.out.println("Added metadata");
                    headers.put(Metadata.Key.of("CLIENT-HEADER", ASCII_STRING_MARSHALLER), "HELLO_I'M_HEADER");
                    super.start(responseListener, headers);
                }
            };
        }
    }
}
