package com.fairandsmart.consent.api;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
        tags = {
                @Tag(name="consent", description="Consent operations."),
                @Tag(name="subject", description="Operations related to subjects")
        },
        info = @Info(
                title="RightConsent API",
                version = "0.2.0",
                contact = @Contact(
                        name = "Right Consent API Community Support",
                        url = "http://exampleurl.com/contact",
                        email = "techsupport@example.com"),
                license = @License(
                        name = "GPL (GNU General Public License) version 3",
                        url = "https://www.gnu.org/licenses/gpl-3.0.txt"))
)
public class RightConsentApplication {
}
