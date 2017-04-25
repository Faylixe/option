# Option

[![Build Status](https://travis-ci.org/Faylixe/option.svg?branch=master)](https://travis-ci.org/Faylixe/option)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/b6ef03db876e4a3f90006ee2d3956d59)](https://www.codacy.com/app/Faylixe/option?utm_source=github.com&utm_medium=referral&utm_content=Faylixe/option&utm_campaign=Badge_Coverage)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/b6ef03db876e4a3f90006ee2d3956d59)](https://www.codacy.com/app/Faylixe/option?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Faylixe/option&amp;utm_campaign=Badge_Grade)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/fr.faylixe/option/badge.svg)](https://maven-badges.herokuapp.com/maven-central/fr.faylixe/option)

**Option** is a simple Java library that allows easy command line option parsing.
It is based on Apache Commons CLI framework, but provides an easier interface
based on annotation and reflection.

In order to use it in your project just use the following Maven dependency :

```xml
<dependency>
    <groupId>fr.faylixe</groupId>
    <artifactId>option</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Basic usage

Considering a Java command line application that could have a boolean flag settled and
take an optional String parameter, using **Option**, such task is easy :

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

All we have to do is to annotate application class attributes with ``@Optionable`` and calls
_bootstrap(String [])_ methods using CLI provided args and the job is done ! Running such application will only
consist in following command :

```bash
javac App.java
java App --flag --value foo
```

## Short option conflict

If you have two attribute starting with the same initial letter, you can explicitly specifying
the short option name to the ``@Optionable`` annotation using ``shortName`` parameters.