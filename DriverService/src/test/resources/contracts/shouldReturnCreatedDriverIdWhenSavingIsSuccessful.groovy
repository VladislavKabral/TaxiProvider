package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        url("/drivers")
        headers {
            contentType applicationJson()
        }
        method POST()
        body(
                lastname: "Leonov",
                firstname: "Kiril",
                email: "mr.leonov@mail.ru",
                phoneNumber: "+375292796152",
                password: "\$2a\$12\$b7CcS8TDc.0Zjc2bZHYFPOfnOvJsR6EDC.PlDloRe3RevAC3jYLDS"
        )
    }

    response {
        status CREATED()
        headers {
            contentType applicationJson()
        }
        body(
                id: 4
        )
    }
}