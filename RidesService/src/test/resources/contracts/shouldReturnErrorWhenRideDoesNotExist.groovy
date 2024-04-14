package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/rides/6")
        method GET()
    }

    response {
        status NOT_FOUND()
        headers {
            contentType applicationJson()
        }
        body(
                message: "Ride with id '6' wasn't found."
        )
    }
}