import openai
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity

# Set your OpenAI API key
openai.api_key = 'no no no no no!!'

# List of products
products = [
    {
        "title": "Wireless Mouse",
        "description": "A comfortable wireless mouse with ergonomic design and long battery life, perfect for seamless connectivity without cables."
    },
    {
        "title": "Wireless Headphones",
        "description": "High-quality wireless headphones with noise-canceling technology and immersive sound, ideal for music lovers and frequent travelers."
    },
    {
        "title": "Bluetooth Speaker",
        "description": "Portable Bluetooth speaker with excellent sound quality and long battery life, suitable for outdoor use and parties."
    },
    {
        "title": "4K Monitor",
        "description": "A 27-inch 4K UHD monitor with vibrant colors and high dynamic range for stunning visuals, providing an exceptional viewing experience."
    }
]

# Function to generate embeddings
def get_embedding(text):
    response = openai.Embedding.create(
        input=text,
        model="text-embedding-ada-002"
    )
    return response['data'][0]['embedding']

# Generate embeddings for the product descriptions
product_embeddings = [get_embedding(product["description"]) for product in products]

# Function to find the most similar product
def find_relevant_product(query, product_embeddings, products):
    query_embedding = get_embedding(query)
    similarities = cosine_similarity([query_embedding], product_embeddings)[0]
    most_similar_index = np.argmax(similarities)
    most_similar_product = products[most_similar_index]
    return most_similar_product

# Example queries
queries = [
    "I need wireless headphones with noise cancellation for travel.",
    "I'm looking for a device with excellent sound quality for outdoor use."
]

# Find the most relevant product for each query
for query in queries:
    relevant_product = find_relevant_product(query, product_embeddings, products)
    print(f"Query: {query}")
    print(f"Most relevant product: {relevant_product['title']}")
    print(f"Description: {relevant_product['description']}\n")
