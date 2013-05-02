package com.twitter.finaglezk

class FinaglezkServiceSpec extends AbstractSpec {
  describe("FinaglezkService") {

    // TODO: Please implement your own tests.

    it("sets a key, then gets it") {
      finaglezk.put("name", "bluebird")()
      assert(finaglezk.get("name")() === "bluebird")
      intercept[Exception] { finaglezk.get("what?")() }
    }
  }
}
