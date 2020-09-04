## Integration use cases

### CGU

Display CGU form only once when user have never accessed the web application.
User must accept the CGU to access the application. If cgu have been accepted previously, the form does not appear.
Form can be display using redirect, popup or iframe.

### Language and timezone preference 

Ask user for language and timezone preference at first connection on the application. 
User can bypass the choices.
User can change its preference via a dedicated page.
A simple language button (no form, direct api call) can be used to change the preference.

### Consent collect at account creation for using email, address, phone number...

Display consent form for treatments regarding email and other personal data given during registration.
Treatments could be marketing (promotion), service proposal (training, labs, tests), feedback...
Some preferences choices could also be done at this moment (preferred channel for notifications, language, currency, timezone, ...)
All consent and preferences can also be accessible in the user profile management section (maybe including other consents) 

### Consent collect to access a part of the application

Given an application that focus on specific user data (analysis service, research program).
Consent is collected at application startup or acces to the application web site section.
Consent is given to access the application, consent revocation may not be possible or may block access to application.

### Global consent modification page

Given a user profile space.
All preferences, CGU and consent are centralized in a dedicated page that allows to modify some of those consent / preferences

### An operator can change consent of a customer

Given a customer and an opertor acting in his name, 
The operator has access to a page that display all records of a user and allow to make modifications.
Modifications are stacked in a transaction scope that can be submited at any time.
A receipt is generated for consented treatments and that receipt is forwarded to customer via any kind of channel (email, user space, cloud (drive), download link, ...) 
The receipt allows customer to see all effective consent records given and contains a link to change that things.

 

 




