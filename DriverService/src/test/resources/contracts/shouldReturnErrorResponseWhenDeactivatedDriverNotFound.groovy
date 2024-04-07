package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/drivers/4")
        headers {
            contentType applicationJson()
        }
        method DELETE()
    }

    response {
        status NOT_FOUND()
        headers {
            contentType applicationJson()
        }
        body(
                message: "Driver with id '4' wasn't found."
        )
    }
}