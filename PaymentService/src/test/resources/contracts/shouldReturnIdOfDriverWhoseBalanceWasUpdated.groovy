package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/payment/customers/drivers/1")
        headers {
            contentType applicationJson()
        }
        method PATCH()
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