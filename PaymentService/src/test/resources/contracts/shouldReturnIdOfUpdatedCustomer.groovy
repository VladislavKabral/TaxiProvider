package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/payment/customers/1")
        headers {
            contentType applicationJson()
        }
        method PATCH()
        body(
                name: 'Ivan Ivanov',
                email: 'mr.ivanov@mail.ru',
                phone: '+375291234567',
                taxiUserId: 1,
                balance: 100.0,
                role: 'PASSENGER'
        )
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body(
            id: 'wqjhfqkjfhqf63jkbn'
        )
    }
}