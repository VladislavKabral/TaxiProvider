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
                phoneNumber: "%gghh7"
        )
    }

    response {
        status BAD_REQUEST()
        headers {
            contentType applicationJson()
        }
        body(
                message: "Wrong phone number format."
        )
    }
}