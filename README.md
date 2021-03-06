## Parallax Effect in Compose Image

![](https://github.com/yeocak/ComposableParallaxImage/blob/main/forgithub/preview_gif.gif)

----

### Example usage

	// In an activity or fragment...
	private lateinit var gravitySensorDefaulted: GravitySensorDefaulted

	override fun onStart() {
		super.onStart()
	}

	override fun onStop() {
		super.onStop()
		gravitySensorDefaulted.stop()
	}

	@Composable
	private fun ParallaxEffectImages() {
		// Use same sensor for all parallaxImage in same page.
		ParallaxImage(
			image = R.drawable.test_photo,
			modifier = Modifier.weight(1f),
			sensor = gravitySensorDefaulted
		)
	}

------

### Features

- [x] Creating class instance for motion sensor
- [ ] Creating class instance for parallax function (maybe)
- [ ] Add glide option
- [ ] Add picasso option
- [ ] Better blur

-----

### Add to your project

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
Add the dependency in app.gradle:

	dependencies {
	        implementation 'com.github.yeocak:ComposableParallaxImage:0.5'
	}
