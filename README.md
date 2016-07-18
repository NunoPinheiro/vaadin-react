vaadin-react
==============

Vaadin-React is a tool to create serverside Vaadin components which enable the usage of React components.

The project is still recent and any suggestion will be considered.

## Usage

1 - Install the tool using `npm install -g vaadin-react`

2 - Configure your project with a `.react-vaadin.config` config file. This is a json file with two fields:
 - components : an array of file or dir paths to process your components
 - package : The package to use when generating the java files

3 - Run vaadin-react in your project to generate the resources. This tool expects a project which is a mix of maven and npm. Please check the samples.

## Contributing
Please open any issue that you find. You can also make suggestions to improve the current behaviour.

## Known Limitations

- This project uses react-docgen to parse the components. It can only be used in components compatible with that project.
- Server-side functions cannot return values to the components since that would imply blocking the browser until the answer was received.
