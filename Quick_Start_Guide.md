# Introduction #

SocialPeek can generate random data, or targeted data, if the service allows it, you can target specific users as well, Here is a quick guide to some of the functionality, please read the API for more information.

# Details #

You can use the pre-defined services (Blinkx, Blogger, Bloglines, Delicious, Digg, Flickr, LastFM, Live Search, Technorati, Truveo, Twingly, Twitter, We Feel Fine, Wordpress, Yahoo Search & Youtube) or you can create your own services by extending the framework). Let's focus on using the existing services for now. All Services implement the `SocialService` interface, (and all of them extend the `AbstractSocialService` class)

```

SocialService blinkxService = new BlinkxService();
SocialService bloggerService = new BloggerService();
SocialService bloglinesService = new BloglinesService();
SocialService deliciousService = new DeliciousService();
SocialService diggService = new DiggService();
SocialService flickrService = new FlickrService();
SocialService lastFMService = new LastFMService();
SocialService liveService = new LiveService();
SocialService technoratiService = new TechnoratiService();
SocialService truveoService = new TruveoService();
SocialService twinglyService = new TwinglyService();
SocialService twitterService = new TwitterService();
SocialService weFeelFineService = new WeFeelFineService();
SocialService wordPressService = new WordPressService();
SocialService yahooSearchService = new YahooSearchService();
SocialService youTubeService = new YouTubeService(); 

/* twitter service requires a username and password */
twitterService.setUsername("twitteruser");
twitterService.setPassword("twitterpassword");
		
```

Once you have set up which services you want to use for your app, you need to then set
up your configuration and register those services with your app. Your configuration
is used to contain your registered services, as well as define what type of data you want back (this can be JSON data, RSS data, or good old fashioned vanilla XML). You can also get the raw POJO objects back as well if you want to manipulate them further - we will look at that after this.

```
		
		
Configurable config = new SocialPeekConfiguration();

/* I want RSS data (but could be RETURN_JSON or RETURN_XML */ as well.
config.setFeedType(SocialPeek.RETURN_RSS);
config.registerService(this.blinkxService);
config.registerService(this.bloggerService);
config.registerService(this.bloglinesService);
config.registerService(this.deliciousService);
config.registerService(this.diggService);
config.registerService(this.flickrService);
config.registerService(this.lastFMService);
config.registerService(this.liveService);
config.registerService(this.technoratiService);
config.registerService(this.truveoService);
config.registerService(this.twinglyService);
config.registerService(this.twitterService);
config.registerService(this.weFeelFineService);
config.registerService(this.wordPressService);
config.registerService(this.yahooSearchService);
config.registerService(this.youTubeService);
		
```

Once you have your configuration you can then create a new SocialPeek engine. The engine allows you to call a PeekFactory, this factory will then generate your 'peeker' that will look into the service you want (or a random one, again, it's your call)

```
		
/* set up our main SocialPeek object */
SocialPeek socialPeek = new SocialPeek(config);// pass in our configuration object
PeekFactory peekFactory = socialPeek.getPeekingFactory();
		
```

Now we can start peeking! remember every peek throws a SocialPeekException object, so
make sure you catch it!

```

try {
	String data;

	/* get a random peek from a random service */
	data = peekFactory.getPeeker().getRandomPeek();

	/* get a random peek from a random service using a tag */
	data = peekFactory.getPeeker().getRandomPeekUsingTag("community")


	/* get a random peek from a random service using multiple tags */
	data = peekFactory.getPeeker().getRandomPeekUsingTags(new String[]{"happy","cool","feet"});

	/* get multiple peeks from a random service using a tag, */
	data = peekFactory.getPeeker().getRandomPeekUsingTag("community", 10);

	/* get multiple peeks from twitter */
	data = peekFactory.getPeeker(TwitterService.class).getRandomPeekUsingTag("community", 10);

 	/* get a random peek by a user from del.icio.us */
	data = peekFactory.getPeeker(DeliciousService.class).getUserPeek("someuser");


 	/* get multiple peeks by a user from del.icio.us */
	data = peekFactory.getPeeker(DeliciousService.class).getUserPeek("someuser",10);

```

There are many more options, please check the API for all of them. As I mentioned above, the factory will generate your output format of choice (JSON, RSS or XML). However if you want the raw POJO to work with, then there are methods like `getRawDataRandomPeek()` or `getRawMultiplePeekUsingTag()` available. They will return either a single `Data` object or a `List<Data>` filled with objects. You can then use as you wish.

That should get you going, if you need any help please email me (Dave) and I will help you out. If you find a bug, why not join the project and check out the source!.

Happy Peeking.

- Dave Shanley
(david.shanley@europe.mccann.com)
http://www.socialpeek.com