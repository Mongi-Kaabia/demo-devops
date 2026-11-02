
package com.beedigital.beestore;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class BeeStoreApplicationTest {

    @Test
    void main_demarrerApplicationSansErreur() {
        assertDoesNotThrow(() ->
                BeeStoreApplication.main(new String[]{
                        "--spring.main.web-application-type=none",
                        "--spring.main.banner-mode=off"
                })
        );
    }
}
