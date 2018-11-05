# Create User Pool:
COGNITO_USER_POOL_ID=$(aws cognito-idp create-user-pool --pool-name demo-pool \
--policies 'PasswordPolicy={MinimumLength=6,RequireUppercase=false,RequireLowercase=false,RequireNumbers=false,RequireSymbols=false}' \
--query 'UserPool.Id' --output text)
echo The cognito user pool id is: $COGNITO_USER_POOL_ID

# Define an 'app' that is allowed to use the pool:
COGNITO_USER_POOL_CLIENT_ID=$(aws cognito-idp create-user-pool-client \
--user-pool-id $COGNITO_USER_POOL_ID --client-name 'my client' --generate-secret --explicit-auth-flows ADMIN_NO_SRP_AUTH \
--query 'UserPoolClient.ClientId' --output text)
echo The user pool client ID is: $COGNITO_USER_POOL_CLIENT_ID 

# Get the 'client secret' for this app:
COGNITO_USER_POOL_CLIENT_SECRET=$(aws cognito-idp describe-user-pool-client \
--user-pool-id $COGNITO_USER_POOL_ID --client-id $COGNITO_USER_POOL_CLIENT_ID \
--query 'UserPoolClient.ClientSecret' --output text )
echo The user pool client secret is: $COGNITO_USER_POOL_CLIENT_SECRET

aws cognito-idp admin-create-user --user-pool-id $COGNITO_USER_POOL_ID --username robert --temporary-password robert \
--query 'User.Username' 

# Cleaning up:
#echo Removing cognito user pool $COGNITO_USER_POOL_ID
#aws cognito-idp delete-user-pool --user-pool-id $COGNITO_USER_POOL_ID






