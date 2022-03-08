

Lessons learned with GraphQL.

This project doesn't use the new Spring GraphQL project.  
It uses GraphQL Java

Graph QL is more sophisticated from the caller's perspective.  It requires a more sophisticated client.  I used firecamp on windows.

A working query looks like this:
{
  restaurantByMerchantNumber(merchantNumber: "1234567890") {
    merchantNumber,
    name,
  entityId
  }
}

The "DataFetcher" is like an adapter.  Mine calls the restaurant repository to make something work.

The terms "Query", "Type", and "Field" appear to be used interchangeably.  For example "bookById" is listed under query, but they state this is a field.  I think "Query" is not a reserve word or anything, so when you want to make custom queries you have to make a "Query" type with "fields" representing the different retrieval methods.