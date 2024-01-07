# Rule Structure

The rule structure defines validation rules for different data types within a JSON object. Each data type has its set of rules, and the structure is hierarchical.

## Recognized rules

Each rule must be defined in a JSON format.
Some rules are expected independent of the type

- `key_match:string_regex`: Uses to describe field-specific rule by match using provided regex
- `name_pattern:string_regex`: Validates if field value matches the expected using provided regex

Example:
```json
{
  "key_match": "num.*",
  "name_pattern": "coord.*s"
}
```

Currently, 6 different validators are recognized:

4 primitive 
`Number`, `String`, `Boolean`, `DateTime`

2 abstract
`Array`, `Object` which contain other (both primitive and abstract) validators inside.

When specifying rules for specific type input, validator type **must** be the key.
The following describes recognized validation rules by validator type

## Number Rules

- `min:number`: Validates if the numeric value is greater than the provided one.
- `max:number`: Validates if the numeric value is lower than the provided one.
- `equal:number`: Validates if the numeric value is equal to the provided one
- `even:boolean`: Validates if the numeric value is even. For floating point numbers, it is `false` [adjust]
- `odd:boolean`: Validates if the numeric value is odd. For floating point numbers, it is `false` [adjust]
- `enum:array_number`: Validates if the number value matches one of the provided in the array


Single rule example:
```json
"number": {
    "key_match": "num.*",
    "min": 10,
    "even": true
}
```

```json
"number": {
    "key_match": "numx.*",
    "equal": 51
}
```

It is also possible to provided multiple rules for `number` input.

Example:
```json
"number": [
    {
      "key_match": "num.*",
      "min": 10,
      "even": true
    },
    {
      "key_match": "magic.*",
      "max": 910
      "odd": true  
    },
    {
      "key_match": "y.*",
      "min": 563,
      "even": false
    },    
  ]
```

## String Rules

- `value_match:string_regex`: Validates if the string value matches the provided regex
- `enum:array_string`: Validates if the string value matches one of the provided in the array

Similar to `Number` rules, single rule example:
```json
"string": {
  "key_match": "str.*",
  "value_match": ".*t",
}
```

It is also possible to provided multiple rules for `string` input.

```json
"string": [
    {
      "key_match": "title",
      "value_match": ".t",
    },
    {
      "key_match": "altitudeUnitofMeasure",
      "enum": ["m", "ft"]
    }
]
```

## Boolean Rules

- `expected:boolean`: Validates if the boolean value matches the expected one

Single rule example:
```json
"boolean": {
  "key_match": "bool",
  "expected": false
}
```

It is also possible to provided multiple rules for `boolean` input.

```json
"boolean": [
    {
      "key_match": "boo.*",
      "expected": false
    },
    {
      "key_match": "abal.*",
      "expected": true
    }
]
```

## Datetime Rules

To be validated, datetime must conform to [ISO 8601](https://en.wikipedia.org/wiki/ISO_8601) without offsets or to one of the following formats
```
yyyy-MM-dd HH:mm:ss
dd/MM/yyyy HH:mm:ss
dd.MM.yyyy HH:mm:ss
```

- `day_of_week:string`: Validates if the string datetime day of week equals the provided one. The provided datetime**must** match one of days of the week (independent of the case)
- `before:string`: Validates if the string datetime is before the provided one
- `after:string`: Validates if the string datetime is after the provided one

Single rule example:
```json
"date": {
  "key_match": "dat.*",
  "day_of_week": "monday"
}
```

It is also possible to provided multiple rules for `date` input.

```json
"date": [
    {
      "key_match": "da.*",
      
    },
    {
      "key_match": "magic_da.*",
      "before": "2023-02-08"
    }
]
```

## Array Rules

- `contains:any`: Validates if the array contains the item provided.
- `type:string`: Validates if the array has items only of the provided type. Type is one of `string`, `number`, `boolean`, `date`

Single rule example:
```json
"array": {
  "key_match": "arr.*",
  "type": "number"
}
```

Array rule allows to nest other validators inside, without `key_match` aspect. [adjust]

Example: 

```json
"array": {
    "key_match": "array",
    "contains": "VAX",
    "string": {
      "value_match": 3
    },
    "number": {
      "max": 10
    }
}
```

## Object Rules

Object does not have validator-specific rules, but can contain nested rules.

Validators are specified under the `validators` keyword

Single rule example:
```json
"object": {
    "key_match": "nestedObject",
    "validators": {
      "string": {
        "key_match": "mama.*",
        "value_match": ".*t"
      },
      "object": {
        "key_match": "object",
        "validators": [
          {
            "string": {
              "key_match": "ra.*",
              "value_match": ".*t"
            }
          }
        ]
      }
    }
  }
```

