package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/passengers")
        method GET()
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body(
                content: [
                        [
                            id: 1,
                            lastname: "Ivanov",
                            firstname: "Ivan",
                            email: "mr.ivanov@mail.ru",
                            phoneNumber: "+375291234590"
                        ],
                        [
                            id: 2,
                            lastname: "Petrov",
                            firstname: "Petr",
                            email: "mr.petrov@mail.ru",
                            phoneNumber: "+375336894867"
                        ],
                        [
                            id: 3,
                            lastname: "Borisov",
                            firstname: "Boris",
                            email: "mr.borisov@mail.ru",
                            phoneNumber: "+375296131124"
                        ]
                ]
        )
    }
}