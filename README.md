# Option

[![Build Status](https://travis-ci.org/Faylixe/option.svg?branch=master)](https://travis-ci.org/Faylixe/option)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/b6ef03db876e4a3f90006ee2d3956d59)](https://www.codacy.com/app/Faylixe/option?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Faylixe/option&amp;utm_campaign=Badge_Grade)


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