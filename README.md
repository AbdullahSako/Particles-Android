<h1 align="center">
  Particles-Android
</h1>

<h4 align="center">A collection of views pertaining particles.</h4>

<p align="center">
    <a href="https://opensource.org/licenses/ISC"><img alt="License" src="https://img.shields.io/badge/License-ISC-yellow.svg"/></a>
    <img alt="API level 24" src="https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat"/></a>
</p>


<div align="center">
<img src="https://raw.githubusercontent.com/AbdullahSako/Particles-Android/master/gifs/sparkles.gif" width="24%"/>
<img src="https://raw.githubusercontent.com/AbdullahSako/Particles-Android/master/gifs/fireworks.gif" width="24%"/>
<img src="https://raw.githubusercontent.com/AbdullahSako/Particles-Android/master/gifs/snowfall.gif" width="24%"/>
<img src="https://raw.githubusercontent.com/AbdullahSako/Particles-Android/master/gifs/spacetravel.gif" width="24%"/>
</div>


## Getting started
Add jitpack to settings.gradle

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
```


Add to your dependencies:

Groovy
```groovy
dependencies {
    implementation 'com.github.AbdullahSako:Particles-Android:1.0'
}
```

Kotlin
```kotlin
dependencies {
    implementation ("com.github.AbdullahSako:Particles-Android:1.0")
}
```



Find latest version and release notes [here](https://github.com/AbdullahSako/Particles-Android/releases)

## Usage

<p  align="center"><a href="https://github.com/AbdullahSako/Particles-Android/tree/master/app"><b>-Check out the sample app-</b></a></p>

### SparklesView

<div align="center">
<img src="https://raw.githubusercontent.com/AbdullahSako/Particles-Android/master/gifs/sparkles.gif" width="25%"/>
<img src="https://raw.githubusercontent.com/AbdullahSako/Particles-Android/master/gifs/sparklesConnect.gif" width="25%"/>
</div>

```xml
    <com.sako.particles.ui.sparkles.SparklesView
        android:id="@+id/sparklesView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

- `particlesCount` - **Int (default: 100)**: The number of particles to be drawn inside the view.
- `particlesColor` - **color (default: Green)**: The Color for all of the particles.
- `randomColor` - **boolean (default: false)**: Color each one of the particles with a random color (this overrides `particlesColor`).
- `enableTopFadingEdge` - **boolean (default: false)**: Add a fading edge along the top edge of the view.
- `enableBottomFadingEdge` - **boolean (default: false)**: Add a fading edge along the bottom edge of the view.
- `enableLeftFadingEdge` - **boolean (default: false)**: Add a fading edge along the left edge of the view.
- `enableRightFadingEdge` - **boolean (default: false)**: Add a fading edge along the right edge of the view.
- `fadeWidth` - **float (default: 100f)**: Width of the fading edges.
- `fadeColor` - **color (default: White)**: Color of the fading edges.
- `enableParticleConnect` - **boolean (default: false)**: draw lines between particles.
- `connectLineColor` - **color (default: Red)**: The color of the lines that connect the particles.
- `maxParticleConnectDistance` - **float (default: 100f)**: The maximum distance between two particles for a connecting line to be drawn between them.
- `enableTouchToAccelerate` - **boolean (default: true)**: enable touch to accelerate particles on view click.
- `maxVelocity` - **float (default: 0.5)**: The maximum velocity of the particles, calculated by random(-`maxVelocity` to `maxVelocity`).
- `maxAcceleration` - **float (default: 1.33)**: The maximum acceleration of the particles on view click.
- `maxSize` - **float (default: 3f)**: The maximum size of the particles, calculated by random(1f to `maxSize`).
- `sparkleViewBackgroundColor` - **color (default: transparent)**: The background color of the view.
- `radius` - **float (default: 0f)**: The corner radius of the view.


### FireworksView

<div align="center">
<img src="https://raw.githubusercontent.com/AbdullahSako/Particles-Android/master/gifs/fireworks.gif" width="250"/>
</div>

```xml
    <com.sako.particles.ui.fireworks.FireworksView
        android:id="@+id/fireworksView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

- `maxFireworkCount` - **Int (default: 10)**: The maximum number of live firework particles.
- `mainParticleColor` - **color (default: White)**: The color of a firework main (body) particle.  
- `mainParticleMinVelocity` - **float (default: 15)**: The minimum velocity of main (body) particle, velocity calculated by random(`mainParticleMinVelocity` to `mainParticleMaxVelocity`)
- `mainParticleMaxVelocity` - **float (default: 25)**: The maximum velocity of main (body) particle, velocity calculated by random(`mainParticleMinVelocity` to `mainParticleMaxVelocity`)
- `mainParticleSize` - **float (default: 3)**: The size of a firework main (body) particle.
- `mainParticleTrailCount` - **Int (default: 5)**: The length of a firework main (body) particle trail.
- `explosionParticlesCount` - **Int (default: 20)**: The number of particles in a firework explosion.
- `explosionParticlesColor` - **color (default: Red)**: The color of a firework explosion particles (must disable `explosionRandomColor` first to work).
- `explosionRandomColor` - **boolean (default: true)**: Set the color of each explosion to a random color.
- `explosionParticleRandomColor` - **boolean (default: false)**: Set the color of each particle in an explosion to a random color.
- `explosionMinSize` - **float (default: 2)**: The minimum size of an explosion particle, size calculated by random(`explosionMinSize` to `explosionMaxSize`)
- `explosionMaxSize` - **float (default: 8)**: The maximum size of an explosion particle, size calculated by random(`explosionMinSize` to `explosionMaxSize`)
- `explosionAreaMaxSize` - **Int (default: -1(AUTO))**: The maximum size of the explosion area, The explosion particles fade when outside the area, when set to -1 (AUTO) the area is calculated by the number of the fireworks and View size.
- `explosionTrailCount` - **Int (default: 6)**: The length of a firework explosion particle trail.
- `explosionSmudge` - **boolean (default: false)**: enable smudge effect on firework explosion.

### SnowfallView


<div align="center">
<img src="https://raw.githubusercontent.com/AbdullahSako/Particles-Android/master/gifs/snowfall.gif" width="250"/>
</div>

```xml
    <com.sako.particles.ui.snowfall.SnowfallView
        android:id="@+id/snowFallView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

- `snowParticleCount` - **Int (default: 400)**: The maximum number of snow particles on the screen. 
- `minParticleSize` - **float (default: 1)**: The minimum size of a snow particle, size caluclated by random(`minParticleSize` to `maxParticleSize`).
- `maxParticleSize` - **float (default: 5)**: The maximum size of a snow particle, size caluclated by random(`minParticleSize` to `maxParticleSize`).
- `particleColor` - **color (default: White)**: The color of all snow particles.
- `randomColor` - **boolean (default: false)**: Color each one of the snow particles with a random color.
- `randomStartPositions` - **boolean (default: true)**: if true place snow particles randomly within the screen, else if false place snow particles at the top out of the screen before starting the animation.


### SpaceTravelView

<div align="center">
<img src="https://raw.githubusercontent.com/AbdullahSako/Particles-Android/master/gifs/spacetravel.gif" width="250"/>
</div>

```xml
    <com.sako.particles.ui.spaceTravel.SpaceTravelView
        android:id="@+id/spaceTravelView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

- `starParticleCount` - **Int (default: 150)**: The maximum number of star particles on the screen. 
- `starTrailCount` - **Int (default: 4)**: The length of a star particle trail. 

- `startAnimation()`: Starts the animation of the particles. 
- `stopAnimation()`: stops the animation of the particles.

Call startAnimation() to start the animation of the view
```kotlin
 binding.spaceTravelView.startAnimation()
```

## License

Particles-Android is released under the ISC license. See [LICENSE](https://github.com/AbdullahSako/Particles-Android/blob/master/LICENSE) for details.
