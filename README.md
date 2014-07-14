cloud-print-console
===================

インストール手順
----------------

### GlassFish インストール

```sh
$ wget http://download.java.net/glassfish/4.0/release/glassfish-4.0.zip
$ unzip glassfish-4.0.zip
$ export GLASSFISH_HOME=<GLASSFISH_HOME>
```

### Maven インストール

```sh
$ wget http://ftp.meisei-u.ac.jp/mirror/apache/dist/maven/maven-3/3.2.2/binaries/apache-maven-3.2.2-bin.tar.gz
$ tar xfz apache-maven-3.2.2-bin.tar.gz
$ export M2_HOME=<M2_HOME>
```

### Git クローン

```sh
$ git clone https://github.com/akihyro/cloud-print-console.git
$ cd cloud-print-console
```

### ドメイン 作成

```sh
$ mvn glassfish:create-domain
```

起動手順
--------

### ドメイン 起動

```sh
$ mvn glassfish:start-domain
```

### WAR 作成

```sh
$ mvn war:war
```

### WAR デプロイ

```sh
$ mvn glassfish:deploy # or glassfish:redeploy
```

停止手順
--------

### ドメイン 停止

```sh
$ mvn glassfish:stop-domain
```
