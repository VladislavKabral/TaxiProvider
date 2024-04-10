package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/drivers/1/profile")
        method GET()
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body(
                driver: [
                        id: 1,
                        lastname: "Vasiliev",
                        firstname: "Platon",
                        email: "mr.vasiliev@mail.ru",
                        phoneNumber: "+375293660893",
                        accountStatus: "ACTIVE",
                        status: "FREE",
                        balance: 0.0
                ],
                rating: 5.0
        )
    }
}