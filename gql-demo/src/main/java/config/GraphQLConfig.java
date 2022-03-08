package config;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

import java.io.IOException;
import java.net.URL;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import rewards.internal.restaurant.JpaRestaurantRepository;
import rewards.internal.restaurant.Restaurant;
import rewards.internal.restaurant.RestaurantRepository;


@Configuration
public class GraphQLConfig {


	@Bean
	public RestaurantRepository restaurantRepository() {
		return new JpaRestaurantRepository();
	}
	

    @Bean
    public GraphQL graphQL() throws IOException {
    	
    	//	Read static file to get type definitions:
        URL url = Resources.getResource("schema.graphqls");
        String sdl = Resources.toString(url, Charsets.UTF_8);
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);

        //	Match types up with dataFetchers:
        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
            .type(newTypeWiring("Query")
            	.dataFetcher("restaurantByMerchantNumber", 
            	getRestaurantByMerchantNumberDataFetcher()))
            .build();

        //	Make schema:
        GraphQLSchema graphQLSchema = 
        	new SchemaGenerator()
        	.makeExecutableSchema(typeRegistry, runtimeWiring);
        		
        return GraphQL.newGraphQL(graphQLSchema).build();
    }

    private DataFetcher<Restaurant> getRestaurantByMerchantNumberDataFetcher() {
        return dataFetchingEnvironment -> {
            String number = dataFetchingEnvironment.getArgument("merchantNumber");
            return restaurantRepository().findByMerchantNumber(number);
        };
    }    

    
}
