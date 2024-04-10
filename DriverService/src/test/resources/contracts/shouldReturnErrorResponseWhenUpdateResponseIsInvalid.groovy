package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/drivers/1")
        headers {
            contentType applicationJson()
        }
        method PATCH()
        body(
                id: 1,
                lastname: "Vasiliev",
                firstname: "Platon",
                email: "mr.vasiliev@mail.ru",
                phoneNumber: "+375293660893",
                accountStatus: "ACTIVE",
                status: "lwekljwf",
                balance: 0.0
        )
    }

    response {
        status BAD_REQUEST()
        headers {
            contentType applicationJson()
        }
        body(
                message: "Invalid ride status for driver."
        )
    }
}