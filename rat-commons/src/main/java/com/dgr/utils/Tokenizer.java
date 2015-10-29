
/**
 * Document   : Tokenizer
 * Created on : Oct 20, 2009, 9:57:15 PM
 * Author     : Daniele Grignani
 */

package com.dgr.utils;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer implements Iterator {
    private CharSequence _input;
    private Matcher _matcher;
    private boolean _returnDelims;
    private String _delim;
    private String _match;
    private int _lastEnd = 0;

    public Tokenizer(CharSequence input, String patternStr, boolean returnDelims) {
        this._input = input;
        this._returnDelims = returnDelims;

        Pattern pattern = Pattern.compile(patternStr);
        _matcher = pattern.matcher(input);
    }

    public boolean hasNext() {
        if (_matcher == null) {
            return false;
        }
        if (_delim != null || _match != null) {
            return true;
        }
        if (_matcher.find()) {
            if (_returnDelims) {
                _delim = _input.subSequence(_lastEnd, _matcher.start()).toString();
            }
            _match = _matcher.group();
            _lastEnd = _matcher.end();
        }
        else if (_returnDelims && _lastEnd < _input.length()) {
            _delim = _input.subSequence(_lastEnd, _input.length()).toString();
            _lastEnd = _input.length();

            _matcher = null;
        }

        return _delim != null || _match != null;
    }

    public Object next() {
        String result = null;

        if (_delim != null) {
            result = _delim;
            _delim = null;
        }
        else if (_match != null) {
            result = _match;
            _match = null;
        }

        return result;
    }

    public boolean isNextToken() {
        return _delim == null && _match != null;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
