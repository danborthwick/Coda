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

        public function doubledValue() : int
        {
            return 2 * getValue();
        }
    }
}