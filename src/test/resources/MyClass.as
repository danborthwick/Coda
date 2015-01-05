package mypackage
{
    class MyClass
    {
        protected var _value : int;

        public function MyClass(value : int)
        {
            _value = value;
        }

        public function getValue() : int
        {
            return _value;
        }

        private function doubledValue() : int
        {
            return 2 * getValue();
        }

        public function hypotenuseLength(sideALength : Number, sideBLength : Number) : Number
        {
            var hypotenuseSquared : Number = (sideALength * sideALength) + (sideBLength * sideBLength);
            return Math.sqrt(hypotenuseSquared);
        }

        protected function simpleLoop() : void
        {
            var accumulator : int = 0;
            for (var i : int = 0; i < getValue(); i++)
            {
                accumulator += i;
                accumulator += _value;
            }
        }

    }
}