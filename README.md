# Option

Option is a simple Java framework that allows easy command line option parsing.
It is based on Apache Commons CLI framworks, but provides an easier interface
based on annotation.

## Basic usage

Considering a Java command line application that could have a boolean flag settled and
take a String parameter :

```java
public class App extends OptionableApplication {

	@Optionable
	private boolean flag;

	@Optionable
	private String value;

	/** Boostraping option is the only things to do :) **/
	public static void main(final String [] args) {
		new App().bootstrap(args);
	}

	@Override
	public void run() {
		// Your application code goes here, attribute are already filled.
	}

}
```

```bash
javac App.java
java App --flag --value foo
```

## The Optionable annotation

An OptionableApplication class aims to fill attribute with values from command line.
In order to do so such attribute should be annotated using @Optionable annotation.


## Short option conflict
## Application usage
## Contributing