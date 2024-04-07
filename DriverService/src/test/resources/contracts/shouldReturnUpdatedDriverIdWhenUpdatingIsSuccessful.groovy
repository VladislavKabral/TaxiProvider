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
                status: "FREE",
                balance: 0.0
        )
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body(
                id: 1
        )
    }
}