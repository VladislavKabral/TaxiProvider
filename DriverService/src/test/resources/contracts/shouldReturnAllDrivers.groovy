package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/drivers")
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
                            lastname: "Vasiliev",
                            firstname: "Platon",
                            email: "mr.vasiliev@mail.ru",
                            phoneNumber: "+375293660893",
                            accountStatus: "ACTIVE",
                            status: "FREE",
                            balance: 0.0
                        ],
                        [
                            id: 2,
                            lastname: "Dybrovin",
                            firstname: "Ilia",
                            email: "mr.dybrovin@mail.ru",
                            phoneNumber: "+375296499224",
                            accountStatus: "ACTIVE",
                            status: "FREE",
                            balance: 0.0
                        ],
                        [
                            id: 3,
                            lastname: "Smirnov",
                            firstname: "Sergei",
                            email: "mr.smirnov@mail.ru",
                            phoneNumber: "+375298415692",
                            accountStatus: "ACTIVE",
                            status: "FREE",
                            balance: 0.0
                        ]
                ]
        )
    }
}