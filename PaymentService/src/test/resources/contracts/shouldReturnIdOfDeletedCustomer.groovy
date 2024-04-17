package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/payment/customers/1?role=PASSENGER")
        headers {
            contentType applicationJson()
        }
        method DELETE()
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