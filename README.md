![SmallPocket](./app/src/main/res/mipmap-xxxhdpi/ic_launcher.png)

# SmallPocket

[![Join the chat at https://gitter.im/RxKotlin/Pocket](https://badges.gitter.im/RxKotlin/Pocket.svg)](https://gitter.im/RxKotlin/Pocket?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build
Status](https://travis-ci.org/RxKotlin/Pocket.svg?branch=master)](https://travis-ci.org/RxKotlin/Pocket)

This is a first kotlin app, help user to save links easily, and can export to Evernote as weekly.

Steps:

1. copy link anywhere
2. open SmallPocket
3. query save or not
4. click OK
5. input tags for this link
6. click save

![1](./screenshot/1k.png)
![2](./screenshot/2k.png)
![3](./screenshot/3k.png)
![4](./screenshot/4k.png)
![5](./screenshot/5k.png)

## Architectural

### Model
|Link|Tag|
|---|---|
|CoreLink|CoreTag|
|RealmLink|RealTag|

### View
|IMainView|IListView|
|---|---|
|MainActivity|LinkListFragment|

### Presenter
|IMainPresenter|ILinkListPresenter|
|---|---|
|MainPresenter|LinkListPresenter|


## Our Libray writen by Kotlin

An HTML render engin [HtmlTemplateEngine](https://github.com/RxKotlin/HtmlTemplateEngine)

## 3rd Part Libs

* [RxKotlin](https://github.com/ReactiveX/RxKotlin)
* [Realm](https://realm.io/)
* [Volley](https://github.com/mcxiaoke/android-volley)
* [JsoupXpath](https://github.com/zhegexiaohuozi/JsoupXpath)
* [HockeyApp](https://www.hockeyapp.net/)
* [base-adapter-helper](https://github.com/JoanZapata/base-adapter-helper)


## License

```
The MIT License (MIT)
Copyright (c) <year> <copyright holders>

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
