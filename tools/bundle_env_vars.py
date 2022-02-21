import argparse
import json
import os
import sys


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--configuration", required=True)
    parser.add_argument("--output", required=True)
    args = parser.parse_args()

    data = json.loads(open(args.configuration).read())
    
    with open(args.output, 'w') as out:
        for required in data['required']:
            if required not in os.environ:
                print('Env variiable not set:', required)
                sys.exit(1)
            out.write(f"export {required}={os.environ[required]}\n")

        if 'other' in data:
            for other in data['other']:
                if other in os.environ:
                    out.write(f"export {other}={os.environ[other]}\n")


if __name__ == '__main__':
    main()
