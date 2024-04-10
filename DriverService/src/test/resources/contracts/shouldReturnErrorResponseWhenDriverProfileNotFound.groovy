package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/drivers/4/profile")
        headers {
            contentType applicationJson()
        }
        method GET()
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