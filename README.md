vaadin-react
==============

Vaadin-React creates serverside Vaadin components which enable the usage of react components.


## Known Limitations

- This project uses react-docgen to parse the components. It can only be used in components compatible with that project.
- Server-side functions cannot return values to the components since that would imply blocking the browser until the answer was received.
