## Authentication use cases

Backend calls are ALWAYS authenticated which means that if you want to call API from client side code, you have to include authentication token in requests.
Integrating the product in unauthentified use case means that you'll have to introduce a CSRF/proxy component that will enforce authentication before forwarding to backend 

### Anonymous calls

Your client app needs to make anonymous calls to API for any reason.
You have to introduce a proxy that will enforce authentication using a defined account (admin) for all requests.
A secure CSRF Ajax call from the client to that proxy will ensure that authentication is protected and that credentials won't be visible. 

### Global authentication 

All calls are made directly (from front end to consent manager backend) and includes credentials informations.
This use case implies to configure a global authentication system that will be common to all components.
The backend should be configured to succeed in validating provided credentials.
Many authentication systems could be handled

### In the mutli tenant deployment

In case of deploying consent manager for multi tenant service offer.
Using OAuth is recommended.
Each Tenant should have a dedicated instance of Consent Manager. 
If tenant wants to handle its own user base, a proxy 
 




