package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/passengers/1")
        headers {
            contentType applicationJson()
        }
        method PATCH()
        body(
                id: 1,
                lastname: "Ivanov",
                firstname: "Ivan",
                email: "mr.ivanov@mail.ru",
                phoneNumber: "+375291234590"
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