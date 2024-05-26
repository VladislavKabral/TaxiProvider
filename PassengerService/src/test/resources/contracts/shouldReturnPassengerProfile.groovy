package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/passengers/1/profile")
        method GET()
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body(
                passenger: [
                        id: 1,
                        lastname: "Ivanov",
                        firstname: "Ivan",
                        email: "mr.ivanov@mail.ru",
                        phoneNumber: "+375291234590"
                ],
                rating: 5.0
        )
    }
}