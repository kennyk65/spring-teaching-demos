The purpose of this demo is to illustrate word frequency 
counting using the reactive style of programming.

Dissection:

1.	fromPath(new File("words.txt").toPath())

	Build a Flux from the file.
	The resulting Flux consists of a sequence of lines
	illustrated below:
	
	this is a test of the emergency broadcasting system
	if this had been a real emergency
	we would all be dead by now
	
2.		.flatMap(word -> Flux.fromArray(word.split(" ")))

		Generate a new Flux consisting of the words
		from the input flux.
		The resulting Flux consists of 1 word per element,
		as illustrated below:
		
			this
			is
			a
			test
			...
		
3.		.map(item -> new KeyValue(item, 1))

		Generate a new Flux consisting of KeyValue pairs, 
		where each:
			Key is the word itself
			Value is the number 1, indicating the occurrence
			of that word.
		as illustrated below:
		
			[this,1]
			[is,1]
			[a,1]
			[test,1]
			...
			
4.		.groupBy(keyValue -> keyValue.key)	

		Group the KeyValue pairs by key.
		This generates a new Flux consisting of
			A key
			All of the KeyValue pairs from the input flux 
			sharing that key
		as illustrated below:
		
			this,([this,1])
			is,([is,1])
			...
			the,([the,1][the,1])
	
5.	.flatMap(group -> group.reduce(new KeyValue("BaseValue",0), (a, b) -> {
		return new KeyValue(b.key, a.value+b.value);
	}))
		
	From the grouped Flux, generate a new Flux of KeyValue pairs
	whose Key is the key for any group, and whose value is the
	sum of the values of the KeyPairs sharing that key.
	
	For each group in the input Flux, flatMap will yield a KeyValue
	pair consisting of the key shared by all members of the group 
	and the result of reducing the values sharing that key.
	
	The reduce function works as follows.
	It takes an initial value representing the base value for the
	calculation. In this case it's a KeyValue pair whose key is
	"BaseValue" (which we don't need) and a value of 0 (the base
	value for a sum function. This initial KeyValue pair will 
	become "a" in the argument list to the lambda function. "b"
	in the argument list to the lambda function will be the first
	Key/Value pair of the group. These 2 pairs will be combined
	into 1, whose key is the key for the group, and whose value
	is the sum of the initial value and the value of the next
	member of the group (always 1, in this example). This process 
	then repeats for all remaining KeyValue pairs in the group,
	with a being the result of the previous accumulation, and b
	being the next member of the group, until all members of the
	group have been processed.
	
	The resulting flux would look as follows:
	
	[all:1]
	[a:2]
	[been:1]
	[be:1]
	...
	
6.	.subscribe(System.out::println);
	
	Triggers the actual workflow, specifying what to do with each
	of the resulting values from the final Flux.
	