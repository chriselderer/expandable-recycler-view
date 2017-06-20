# Expandable RecyclerView

RecyclerView Adapter & ItemDecoration extensions that make it easy to build and customize an expandable RecyclerView.

This implementation includes support for header, sub header, and content views for each section in your RecyclerView. Sub header views can be shown and hidden at will for customizability.

The Adapter keeps track of each section’s expansion state, which can be Minimized, Expanded, Loading, or Error Loading.
Using the loading and error states, lazy loading of data for each section is easy to implement.

## Example 1

<img src="http://i.imgur.com/GvMU8Xg.gif" width="400px"/>


## Example 2

<img src="http://i.imgur.com/2BEx00E.gif" width="400px"/>


## Installation

TODO: Describe the installation process

## Usage

### 1. Create your adapter

Create an adapter that extends ExpandableRecyclerViewAdapter.

You must provide 6 types to the ExpandableRecyclerViewAdapter. This is annoying, but once its done binding view holders for each section becomes very strait forward to implement for you.

1. Header:              Model class for your section headers
2. SubHeader:           Model class for your section sub headers
3. Content:             Model class for the content rows in each section
4. HeaderViewHolder:    ViewHolder class for your header rows
5. SubHeaderViewHolder: ViewHolder class for your sub header rows
6. ContentViewHolder:   ViewHolder class for the content rows in each section

In the constructor, you must provide the classes of the ViewHolders.

```java
public class MyExpandableRecyclerViewAdapter
        extends ExpandableRecyclerViewAdapter<Header, SubHeader, Content, HeaderViewHolder, SubHeaderViewHolder, ContentViewHolder>
{
    public MyExpandableRecyclerViewAdapter()
    {
        super(HeaderViewHolder.class, SubHeaderViewHolder.class, ContentViewHolder.class);
    }
}
```

### 2. Implement the Expandable Interface

By design, the ExpandableRecyclerViewAdapter forces your subclass to implement methods of the Expandable Interface. These methods mock the workflow for a regular RecyclerView and make it simple to handle header, sub header, and content views.


#### Providing section information and datasources

You provide the ExpandableRecyclerView with the number of sections as well as the model objects for each section. A reference to the model objects you provide here is kept internally and used to populate the RecyclerView.

```java
int getNumberOfSections();                                // return the number of sections in your view
Header getHeaderForSection(int sectionIndex);             // provide the Header object for a section
SubHeader getSubHeaderForSection(int sectionIndex);       // provide the SubHeader model object for a section
List<Content> getContentForSection(int sectionIndex);     // provide an ArrayList() of Content model objects for each section
int getDefaultExpansionStateForSection(int sectionIndex); // provide the default expansion state for each section
```


#### Creating ViewHolders

A specific method is provided to create each type of ViewHolder (header, sub header and content).

```java
HeaderViewHolder createSectionHeaderViewHolder(ViewGroup parent);
SubHeaderViewHolder createSectionSubHeaderViewHolder(ViewGroup parent);
ContentViewHolder createSectionContentViewHolder(ViewGroup parent);
```


#### Binding ViewHolders

A specific method is provided to bind each type of ViewHolder. The holder, model object corresponding to the row, index of the section and expansion state are provided.

```java
void bindSectionHeaderViewHolder(HeaderViewHolder holder, 
                                     Header header,
                                     int sectionIndex,
                                     int expansionState);
void bindSectionSubHeaderViewHolder(…);
void bindSectionContentViewHolder(…);
```


#### Showing and Hiding sub headers

This method is called every time the RecyclerView updates. Use it to provide logic to either show or hide a sub header for a specific section. 

```java
boolean shouldShowSectionSubHeader(int sectionIndex, int expansionState);
```


#### Saving state

This method is used to specify what state you want each section to save as when the RecyclerView is destroyed.
ex. From a loading state, you may wish to return to a minimized state if your loading operation is destroyed aswell.

```java
int getSavedStateForSection(int sectionIndex, int expansionState);
```


### 3. Responding to touch events

#### Row touch events

To respond to a touch event on a row, implement the ExpandableRowOnClickListener interface, and register the implementing class with the ExpandableRecyclerViewAdapter. Specific methods will then automatically be called when each type of view is clicked (header, sub header, content).

```java
this.expandableRowOnClickListener = this;
```

#### Row children touch events

To respond to touch events on a child view in a row, implement the expandableRowSubViewOnClickListener interface, and register the implementing class with the ExpandableRecyclerViewAdapter. Specific methods will then automatically be called when each type of view is clicked (header, sub header, content).

```java
this.expandableRowSubViewOnClickListener = this;
```

### 4. Changing expansion state

To change expansion state of a specific section call one of the four provided methods.

```java
setExpansionStateMinimized(int sectionIndex);
setExpansionStateExpanded(int sectionIndex);
setExpansionStateLoading(int sectionIndex);
setExpansionStateError(int sectionIndex);
```

### 5. Notifying the RecyclerView about data updates

ExpandableRecyclerViewAdapter provides its own update methods, do not call the standard RecyclerView update methods (ex. adapter.notifyDataSetChanged)

```java
void notifyExpandableDataSetChanged();
void notifySectionHeaderChanged(int sectionIndex);
void notifySectionSubHeaderChanged(int sectionIndex);
void notifySectionContentChanged(int sectionIndex);
void notifySectionChanged(int sectionIndex)
```

### 6. Saving expansion state

To save the state of the RecyclerView call the ExpandableRecyclerViewAdapter’s onSaveInstanceState & onRestoreInstance methods from the corresponding methods in your activity.

```java
public class MainActivity
{
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
    }
}
```

### 7. Decorating the views!

To provide item decorations for specific rows, create an ItemDecoration class that extends ExpandableRecyclerViewItemDecoration.
Implement the abstract methods provided:

```java
void getHeaderOffsets(…);
void getSubHeaderOffsets(…);
void getContentRowOffsets(…);
void onDrawSectionHeader(…);
void onDrawSectionSubHeader(…);
void onDrawContent(…);
```

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## License

Published under the MIT License.
