package ru.grpc.simple.server.grpcserver.service;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.apache.commons.lang3.StringUtils;
import ru.grpc.simple.proto.CommonResponse;
import ru.grpc.simple.proto.LoginRequest;
import ru.grpc.simple.proto.LogoutRequest;
import ru.grpc.simple.proto.UserGrpc;
import ru.grpc.simple.proto.util.DataUtil;

public class UserService extends UserGrpc.UserImplBase {
    private static final String ONLINE = "ONLINE";
    private static final String OFFLINE = "OFFLINE";

    @Override
    public void login(LoginRequest request, StreamObserver<CommonResponse> responseObserver) {
        String username = request.getUsername();
        String password = request.getPassword();

        CommonResponse.Builder commonResponseBuilder = CommonResponse.newBuilder();
        if (password.equals(String.valueOf(DataUtil.getCorrectPassword()))) {
            System.out.println(String.format("\"%s\"'s login attempt has succeed", username));

            DataUtil.getUsersMap().put(username, ONLINE);

            commonResponseBuilder
                    .setResponseCode(Status.Code.OK.value())
                    .setResponseMessage("Login SUCCESS");
        } else {
            System.out.println(String.format("\"%s\"'s login attempt has failed", username));

            DataUtil.getUsersMap().remove(username);

            commonResponseBuilder.setResponseCode(Status.Code.UNAUTHENTICATED.value())
                    .setResponseMessage("Login FAILED")
                    .build();
        }

        responseObserver.onNext(commonResponseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void logout(LogoutRequest request, StreamObserver<CommonResponse> responseObserver) {
        String username = request.getUsername();

        CommonResponse.Builder commonResponseBuilder = CommonResponse.newBuilder();
        if (StringUtils.isEmpty(username)
                || StringUtils.isEmpty(DataUtil.getUsersMap().get(username))
                || DataUtil.getUsersMap().get(username).equals(OFFLINE)) {

            commonResponseBuilder
                    .setResponseCode(Status.Code.INVALID_ARGUMENT.value())
                    .setResponseMessage(String.format("Logout FAILED, request is empty or user \"%s\" has been OFFLINE yet",
                                                      username));
        } else {
            DataUtil.getUsersMap().put(username, OFFLINE);

            commonResponseBuilder
                    .setResponseCode(Status.Code.OK.value())
                    .setResponseMessage(String.format("Logout SUCCESS for user: \"%s\"", username));
        }

        responseObserver.onNext(commonResponseBuilder.build());
        responseObserver.onCompleted();
    }
}
